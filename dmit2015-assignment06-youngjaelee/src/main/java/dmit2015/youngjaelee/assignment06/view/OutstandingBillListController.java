package dmit2015.youngjaelee.assignment06.view;

import dmit2015.youngjaelee.assignment06.entity.Bill;
import dmit2015.youngjaelee.assignment06.repository.BillRepository;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Named("outstandingBillListController")
@ViewScoped
public class OutstandingBillListController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private BillRepository _billRepository;

    @Getter
    private List<Bill> billList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {

            billList = _billRepository.list().stream().filter(item -> item.getAmountBalance().compareTo(BigDecimal.ZERO) == 1).sorted(Comparator.comparing(Bill::getDueDate)).sorted(Comparator.comparing(Bill::getPayeeName)).collect(Collectors.toList());

            if(billList.isEmpty()) {
                Messages.addGlobalError("list is empty");
            }
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}