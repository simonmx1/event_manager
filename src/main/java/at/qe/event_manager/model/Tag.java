package at.qe.event_manager.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.data.domain.Persistable;

@Entity
public class Tag implements Persistable<String>, Serializable, Comparable<Tag> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    private String text;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Tag o) {
        return this.text.compareTo(o.text);
    }

    @Override
    public boolean equals(Object tag) {
        if (!(tag instanceof Tag)) return false;
        return this.text.compareTo(((Tag) tag).text) == 0;
    }

    @Override
    public String getId() {
        return getText();
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }
}