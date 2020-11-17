package de.coiaf.footballprediction.testframework.testmodel;

import javax.persistence.*;

@Entity
@Table(name="phones")
public class Telephone {

    public static enum Type {
        HOME, OFFICE, MOBILE;
    }

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name="number")
    private String number;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="type")
    private Type type;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
