package com.github.taylort7147.teleporters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.github.taylort7147.tmod.teleporter.event.TeleporterEventHandler;

public class TeleporterLinkManager implements ITeleporterLinkManager
{
    private static final TeleporterLinkManager instance = new TeleporterLinkManager();
    
    public static TeleporterLinkManager getInstance()
    {
        return instance;
    }
    
    private List<Link> links;
    
    public TeleporterLinkManager()
    {
        this.links = new ArrayList<Link>();
    }

    public TeleporterLinkManager(List<Link> links)
    {
        this.links = new ArrayList<Link>();
        for (Link link : links)
        {
            this.links.add(link);
        }
    }

    public void setLinks(List<Link> links)
    {
        this.links = new ArrayList<Link>(links);
    }

    @Override
    public List<Link> getLinks()
    {
        return new ArrayList<Link>(this.links);
    }

    @Override
    public void register(Link link)
    {
        if(links.contains(link))
        {
            throw new RuntimeException("Link " + link + " is already registered");
        }
        links.add(link);
    }

    @Override
    public void unregister(Link link)
    {
        if(!links.remove(link))
        {
            throw new RuntimeException("Link " + link + " is not registered");
        }
    }

    @Override
    public boolean isEndpointPartOfLink(Endpoint endpoint)
    {
        return links.stream().anyMatch(link -> link.contains(endpoint));
    }

    @Override
    @Nullable
    public Link getLink(Endpoint endpoint)
    {
        Optional<Link> foundLink = links.stream().filter(link -> link.contains(endpoint)).findFirst();
        return foundLink.orElse(null);
    }

    @Override
    @Nullable
    public Endpoint getDestination(Endpoint endpoint)
    {
        Link link = getLink(endpoint);
        return (link == null) ? null : link.getOther(endpoint);
    }
}
