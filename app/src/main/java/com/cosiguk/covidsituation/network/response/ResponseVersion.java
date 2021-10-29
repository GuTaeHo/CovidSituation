package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Version;
import com.google.gson.annotations.SerializedName;

public class ResponseVersion {
    @SerializedName("version")
    private Version version;

    public Version getVersion() {
        if (version == null) {
            version = new Version();
        }

        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
