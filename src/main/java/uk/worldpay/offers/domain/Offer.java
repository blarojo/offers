package uk.worldpay.offers.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * Class Offer
 *
 * Class representing a temporal reduction in price for an item
 *
 * @author laurinf
 *
 */
@Data
@ToString
@EqualsAndHashCode
@Entity
public
class Offer {

    /**
     * the id of the offer
     */
    private @Id @GeneratedValue Long id;

    /**
     * the id of the item associated to the offer
     */
    @Column(name = "item_id",nullable = false)
    private Long itemId;

    /**
     * a friendly description of the offer
     */
    private String description;

    /**
     * the price of the associated item during the period of the offer
     */
    @Column(nullable = false)
    private Double price;

    /**
     * date of creation of the offer
     */
    @Column(name="creation_date")
    private Date creationDate;


    /**
     * starting date of the period when the offer will be applicable
     */
    @Column(name="start_date", nullable = false)
    private Date startDate;

    /**
     * expiring date of the offer
     */
    @Column(name="end_date", nullable=false)
    private Date endDate;

    /**
     * Status of the offer. Can take 4 values : {CREATED,ACTIVE,EXPIRED,CANCELLED}
     */
    @Column(nullable = false)
    private Status status;

    /**
     * Date of cancellation of the offer
     */
    @Column(name="cancel_date")
    private Date cancelDate;

    public Offer() {
        creationDate = new Date();
        this.status = Status.CREATED;
    }

    Offer(Long itemId, String description, Double price, Date startDate, Date endDate) {
        this.itemId = itemId;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        creationDate = new Date();
        this.status = Status.CREATED;
    }

    @PrePersist
    protected void onCreate() {
        creationDate = new Date();
        this.status = Status.CREATED;
    }


    /**
     * Enumeration of the possible status for an Offer.
     * These can be CREATED, ACTIVE, CANCELLED, EXPIRED
     *
     * An offer is in :
     *
     *   CREATED status if it was created but the starting date has not arrived yet
     *   ACTIVE status if it was created and not cancelled and the current date is in the period of applicability [startDate, endDate]
     *   CANCELLED status if the offer was cancelled by the user
     *   EXPIRED if the current date is after the expiration date.
     */
    public enum Status {

        CREATED("c", "CREATED"),
        ACTIVE("a", "ACTIVE"),
        CANCELLED("d", "CANCELLED"),
        EXPIRED("e", "EXPIRED");

        private String shortStatus;
        private String textualStatus;

        Status(String shortStatus, String textualStatus) {
            this.shortStatus = shortStatus;
            this.textualStatus = textualStatus;
        }

        public String getTextualStatus() {
            return this.textualStatus;
        }

        public static Status getStatus(String shortStatus) {
            switch (shortStatus) {
                case "c":
                    return Status.CREATED;
                case "a":
                    return Status.ACTIVE;
                case "d":
                    return Status.CANCELLED;
                case "e":
                    return Status.EXPIRED;
            }
            return null;
        }
    }
}