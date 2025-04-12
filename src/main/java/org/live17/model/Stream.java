package org.live17.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Stream {
    private final String streamerID;

    @JsonCreator
    public Stream(@JsonProperty("streamerID") String streamerID) {
        if (streamerID == null || streamerID.trim().isEmpty()) {
            throw new IllegalArgumentException("StreamerID cannot be null or empty");
        }
        this.streamerID = streamerID;
    }

    public String getStreamerID() {
        return streamerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stream stream = (Stream) o;
        return streamerID.equals(stream.streamerID);
    }

    @Override
    public int hashCode() {
        return streamerID.hashCode();
    }

    @Override
    public String toString() {
        return "Stream{streamerID='" + streamerID + "'}";
    }
}