package com.github.taylort7147.tmod.teleporter.capability;

import java.util.List;

import javax.annotation.Nullable;

import com.github.taylort7147.tmod.teleporter.Endpoint;
import com.github.taylort7147.tmod.teleporter.Link;

public interface ITeleporterData
{
    public void register(Link link);

    public void unregister(Link link);

    public boolean isEndpointPartOfLink(Endpoint endpoint);

    @Nullable
    public Link getLink(Endpoint endpoint);

    @Nullable
    public Endpoint getDestination(Endpoint endpoint);

    public List<Link> getLinks();
}
