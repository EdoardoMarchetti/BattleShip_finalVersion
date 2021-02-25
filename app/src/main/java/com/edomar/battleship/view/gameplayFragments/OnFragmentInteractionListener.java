package com.edomar.battleship.view.gameplayFragments;

import androidx.fragment.app.Fragment;

/** Quest'interfaccia permette di fara COMUNICARE activity e fragment**/

public interface OnFragmentInteractionListener {

    void notifyToChangeFragment(Fragment fragment);

    void notifyToStartMatch();
}
