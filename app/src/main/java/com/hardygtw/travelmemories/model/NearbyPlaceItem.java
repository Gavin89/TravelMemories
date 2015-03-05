package com.hardygtw.travelmemories.model;

import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by beaumoaj on 13/01/15.
 */
public class NearbyPlaceItem implements Serializable {
    /**
     * Serial Number for an RssItem
     */
    private static final long serialVersionUID = 8439445017702637758L;
    /*
     * <item> <title>Labour row grounds Qantas flights</title>
     * <description>Australian airline Qantas grounds all flights with immediate
     * effect over an industrial dispute, stranding thousands of passengers
     * worldwide.</description>
     * <link>http://www.bbc.co.uk/go/rss/int/news/-/news
     * /world-us-canada-15504838</link> <guid
     * isPermaLink="false">http://www.bbc.
     * co.uk/news/world-us-canada-15504838</guid> <pubDate>Sat, 29 Oct 2011
     * 10:53:07 GMT</pubDate> <media:thumbnail width="66" height="49" url=
     * "http://news.bbcimg.co.uk/media/images/56355000/jpg/_56355750_qantasindex.jpg"
     * /> <media:thumbnail width="144" height="81" url=
     * "http://news.bbcimg.co.uk/media/images/56357000/jpg/_56357520_013245230-1.jpg"
     * /> </item>
     */
    private String title;
    private String description;
    private URL link;
    private Date date;
    private URL thumb;
    private boolean isRead;
    private GeoLocation location;


    @Override
    public boolean equals(Object o) {
        if (o instanceof NearbyPlaceItem) {
            if (this == o) return true;
            else {
                NearbyPlaceItem other = (NearbyPlaceItem) o;
                return this.title.equals(other.getTitle()) && this.date.equals(other.getDate())
                        && this.getDescription() == other.getDescription() &&
                        this.link.equals(other.getLink()) &&
                        this.location.equals(other.getLocation());
            }

        } else {
            return false;
        }
    }

    public NearbyPlaceItem() {
        isRead = false;
    }

    public void setRead() {
        isRead = true;
    }

    public void unRead() {
        isRead = false;
    }

    public boolean isRead() {
        return isRead;
    }

    public GeoLocation getLocation() {
        if (location == null) {
            return null;
        } else {
            return location;
        }
    }



    public void setLocation(GeoLocation location) {
        this.location = location;
    }


    public String toString() {
        return title;
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder(title);
        sb.append("\n");
        sb.append(description);
        sb.append("\n");
        sb.append(link.toString());
        sb.append("\nFrom the news on: ");
        sb.append(this.getDate());
        return sb.toString();
    }

    public String toString1() {
        StringBuilder sb = new StringBuilder("<item>\n");
        sb.append("<title>");
        sb.append(title);
        sb.append("</title>\n");
        sb.append("<description>");
        sb.append(description);
        sb.append("</description>\n");
        sb.append("<link>");
        sb.append(link.toString());
        sb.append("</link>\n");
        sb.append("<date>");
        sb.append(date);
        sb.append("</date>\n");
        if (thumb != null) {
            sb.append("<media:thumb>");
            sb.append(thumb.toString());
            sb.append("</media:thumb>\n");
        }
        sb.append("</item>\n");
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE, d MMM yyyy HH:mm:ss z", Locale.UK);
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public URL getThumb() {
        return thumb;
    }

    public void setThumb(URL thumb) {
        this.thumb = thumb;
    }

}