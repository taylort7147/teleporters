package com.github.taylort7147.teleporters;

import java.util.List;

import javax.annotation.Nullable;

public interface ITeleporterLinkManager
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
