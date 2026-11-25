package repository;

import model.Claim;
import java.util.ArrayList;

/**
 * TEMPORARY STUB for Member 1/2's ClaimRepository class (for local compilation/testing).
 */
public class ClaimRepository {
    private ArrayList<Claim> claims;

    public ClaimRepository() {
        this.claims = new ArrayList<>();
    }

    public void addClaim(Claim claim) {
        claims.add(claim);
    }

    public Claim getClaimById(String claimId) {
        for (Claim claim : claims) {
            if (claimId != null && claimId.equals(claim.getClaimId())) {
                return claim;
            }
        }
        return null;
    }

    public ArrayList<Claim> getAllClaims() {
        return new ArrayList<>(claims);
    }
}
