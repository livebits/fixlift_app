package com.novintech.elevator.features.base;

public interface FragmentCallback {
    void onFragmentAttached();

    void onFragmentDetached(String tag);
}
