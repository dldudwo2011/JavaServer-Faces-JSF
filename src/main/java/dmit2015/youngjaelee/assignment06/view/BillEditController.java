package dmit2015.youngjaelee.assignment06.view;

import dmit2015.youngjaelee.assignment06.entity.Bill;
import dmit2015.youngjaelee.assignment06.repository.BillRepository;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Named("currentBillEditController")
@ViewScoped
public class BillEditController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private BillRepository _billRepository;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private Long editId;

    @Getter
    private Bill existingBill;

    @PostConstruct
    public void init() {
        if (!Faces.isPostback()) {
            if (editId != null) {
                Optional<Bill> optionalBill = _billRepository.findOptional(editId);
                optionalBill.ifPresentOrElse(
                        existingItem -> existingBill = existingItem,
                        () -> Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/listOutstanding.xhtml")
                );
            } else {
                Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/listOutstanding.xhtml");
            }
        }
    }

    public String onSave() {
        String nextPage = "";
        try {
            _billRepository.update(existingBill);
            Messages.addFlashGlobalInfo("Update was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Update was not successful.");
        }
        return nextPage;
    }
}