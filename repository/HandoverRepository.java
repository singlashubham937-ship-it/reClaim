package repository;

import model.Handover;
import java.util.ArrayList;

/**
 * Stores and manages Handover records using an ArrayList.
 */
public class HandoverRepository {
    private ArrayList<Handover> handovers;

    public HandoverRepository() {
        this.handovers = new ArrayList<>();
    }

    /**
     * Adds a new handover if the handoverId does not already exist.
     */
    public boolean addHandover(Handover handover) {
        if (handover == null || handover.getHandoverId() == null) {
            return false;
        }
        if (getHandoverById(handover.getHandoverId()) != null) {
            return false;
        }
        handovers.add(handover);
        return true;
    }

    /**
     * Finds a handover by its ID.
     */
    public Handover getHandoverById(String handoverId) {
        if (handoverId == null) {
            return null;
        }
        for (Handover h : handovers) {
            if (handoverId.equals(h.getHandoverId())) {
                return h;
            }
        }
        return null;
    }

    /**
     * Returns all stored handovers.
     */
    public ArrayList<Handover> getAllHandovers() {
        return new ArrayList<>(handovers);
    }

    /**
     * Returns all handovers where the given user is either the finder or the owner.
     */
    public ArrayList<Handover> getHandoversByUserId(String userId) {
        ArrayList<Handover> result = new ArrayList<>();
        if (userId == null) {
            return result;
        }
        for (Handover h : handovers) {
            if (userId.equals(h.getFinderId()) || userId.equals(h.getOwnerId())) {
                result.add(h);
            }
        }
        return result;
    }

    /**
     * Returns all handovers associated with a given item ID.
     */
    public ArrayList<Handover> getHandoversByItemId(String itemId) {
        ArrayList<Handover> result = new ArrayList<>();
        if (itemId == null) {
            return result;
        }
        for (Handover h : handovers) {
            if (itemId.equals(h.getItemId())) {
                result.add(h);
            }
        }
        return result;
    }
}
