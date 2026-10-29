package net.kdt.pojavlaunch.fragments;

import static net.kdt.pojavlaunch.Tools.openPath;
import static net.kdt.pojavlaunch.Tools.shareLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.compose.ui.platform.ComposeView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewTreeLifecycleOwner;

import net.kdt.pojavlaunch.CustomControlsActivity;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.prefs.screens.LauncherPreferenceFragment;
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper;
import net.kdt.pojavlaunch.ui.HomeActions;
import net.kdt.pojavlaunch.ui.SharkLauncherHomeKt;
import net.kdt.pojavlaunch.value.launcherprofiles.LauncherProfiles;
import net.kdt.pojavlaunch.value.launcherprofiles.MinecraftProfile;

import java.io.File;

public class MainMenuFragment extends Fragment {
    public static final String TAG = "MainMenuFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ComposeView composeView = new ComposeView(requireContext());
        SharkLauncherHomeKt.attachHomeContent(composeView, new HomeActions() {
            @Override public void onSettings() {
                Tools.swapFragment(requireActivity(), LauncherPreferenceFragment.class, "SETTINGS_FRAGMENT", null);
            }
            @Override public void onInformation() { }
            @Override public void onInstallJar(boolean customArgs) { runInstallerWithConfirmation(customArgs); }
            @Override public void onOpenDirectory() { openCurrentDirectory(); }
            @Override public void onShareLogs() { shareLog(requireContext()); }
            @Override public void onCustomControls() { startActivity(new android.content.Intent(requireContext(), CustomControlsActivity.class)); }
            @Override public void onWiki() { Tools.openURL(requireActivity(), Tools.URL_HOME); }
            @Override public void onDiscord() { Tools.openURL(requireActivity(), getString(R.string.discord_invite)); }
            @Override public void onEditProfile(com.kdt.mcgui.mcVersionSpinner spinner) { spinner.openProfileEditor(requireActivity()); }
            @Override public void onPrototypeRelease() {
                Tools.openURL(requireActivity(), "https://github.com/tagmansuperduperpower-collab/Shark-Launcher/releases/tag/v0.0.0.1");
            }
        });
        return composeView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Compose needs the Fragment view lifecycle owner when the ComposeView is the
        // fragment root. Set it before FragmentContainerView attaches the view.
        ViewTreeLifecycleOwner.set(view, getViewLifecycleOwner());
    }

    private void openCurrentDirectory() {
        Tools.switchDemo(Tools.isDemoProfile(requireContext()));
        if (Tools.isDemoProfile(requireContext())) {
            Toast.makeText(requireContext(), R.string.toast_not_available_demo, Toast.LENGTH_LONG).show();
            return;
        }
        openPath(requireContext(), getCurrentProfileDirectory(), false);
    }

    private File getCurrentProfileDirectory() {
        String currentProfile = LauncherPreferences.DEFAULT_PREF.getString(
                LauncherPreferences.PREF_KEY_CURRENT_PROFILE, null);
        if (!Tools.isValidString(currentProfile)) return new File(Tools.DIR_GAME_NEW);
        LauncherProfiles.load();
        MinecraftProfile profileObject = LauncherProfiles.mainProfileJson.profiles.get(currentProfile);
        if (profileObject == null) return new File(Tools.DIR_GAME_NEW);
        return Tools.getGameDirPath(profileObject);
    }

    private void runInstallerWithConfirmation(boolean isCustomArgs) {
        if (Tools.isLocalProfile(requireContext()) || Tools.isDemoProfile(requireContext())) {
            Toast.makeText(requireContext(), R.string.toast_not_available_demo, Toast.LENGTH_LONG).show();
            return;
        }
        if (ProgressKeeper.getTaskCount() == 0) {
            Tools.installMod(requireActivity(), isCustomArgs);
        } else {
            Toast.makeText(requireContext(), R.string.tasks_ongoing, Toast.LENGTH_LONG).show();
        }
    }
}
