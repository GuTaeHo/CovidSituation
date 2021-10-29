package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Version;

public interface VersionListener {
    void success(Version version);
    void fail(String message);
}
