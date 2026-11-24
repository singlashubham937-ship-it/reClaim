import model.Handover;
import model.AuditLog;
import java.time.LocalDateTime;

public class TestModels {
    public static void main(String[] args) {
        Handover handover = new Handover(
                "HO123",
                "IT456",
                "CL789",
                "F001",
                "O002",
                LocalDateTime.now(),
                "Library",
                "Returned in good condition",
                false
        );
        System.out.println("Before confirm: " + handover);
        handover.confirm();
        System.out.println("After confirm: " + handover);

        AuditLog log = new AuditLog(
                "LG001",
                "U123",
                "HANDOVER",
                "Item handed over to owner",
                LocalDateTime.now()
        );
        System.out.println(log);
    }
}
