package com.github.taylort7147.tmod.teleporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

public class TeleporterLinkManager
{
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

    public void register(Link link)
    {
        if(links.contains(link))
        {
            throw new RuntimeException("Link " + link + " is already registered");
        }
        links.add(link);
    }
    
    public void unregister(Link link)
    {
        if(!links.remove(link))
        {
            throw new RuntimeException("Link " + link + " is not registered");
        }
    }

    @Nullable
    public Link getEstablishedLink(Endpoint endpoint)
    {
        Optional<Link> link = links.stream().filter(e -> e.contains(endpoint)).findFirst();
        return link.orElse(null);
    }

    public boolean isEndpointPartOfLink(Endpoint endpoint)
    {
        return links.stream().anyMatch(link -> link.contains(endpoint));
    }

    public List<Link> getLinks()
    {
        ArrayList<Link> links = new ArrayList<Link>(this.links);
        return links;
    }

    public void setLinks(List<Link> links)
    {
        this.links = new ArrayList<Link>(links);
    }

    public Link getLink(Endpoint endpoint)
    {
        Optional<Link> foundLink = links.stream().filter(link -> link.contains(endpoint)).findFirst();
        return foundLink.orElse(null);
    }

    public Endpoint getDestination(Endpoint endpoint)
    {
        Link link = getLink(endpoint);
        return (link == null) ? null : link.getOther(endpoint);
    }
}
