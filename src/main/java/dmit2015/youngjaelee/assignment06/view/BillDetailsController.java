package dmit2015.youngjaelee.assignment06.view;

import dmit2015.youngjaelee.assignment06.entity.Bill;
import dmit2015.youngjaelee.assignment06.repository.BillRepository;
import lombok.Getter;
import lombok.Setter;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.omnifaces.util.Faces;

import java.io.Serializable;
import java.util.Optional;

@Named("currentBillDetailsController")
@ViewScoped
public class BillDetailsController implements Serializable {
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
        Optional<Bill> optionalBill = _billRepository.findOptional(editId);
        optionalBill.ifPresentOrElse(
                existingItem -> existingBill = existingItem,
                () -> Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/listOutstanding.xhtml")
        );
    }
}