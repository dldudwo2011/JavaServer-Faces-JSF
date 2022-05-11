package dmit2015.youngjaelee.assignment06.repository;


import dmit2015.youngjaelee.assignment06.entity.Bill;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import common.jpa.AbstractJpaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class BillRepository extends AbstractJpaRepository<Bill, Long> {

    public BillRepository() {
        super(Bill.class);
    }

    public List<Bill> getOutstandingbillsWithin3Days(){

        List<Bill> outStandingBillList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        LocalDate threeDaysAfterCurrentDate = currentDate.plusDays(3);

        final String jpql = """
        SELECT p
        FROM Bill p
        WHERE p.dueDate  <= :threeDaysAfterCurrentDate AND p.dueDate >= :currentDate
              AND p.amountBalance > 0
        """;

        TypedQuery<Bill> query = getEntityManager().createQuery(jpql, Bill.class);

        query.setParameter("threeDaysAfterCurrentDate", threeDaysAfterCurrentDate);
        query.setParameter("currentDate", currentDate);

        outStandingBillList = query
                .getResultList().stream().sorted(Comparator.comparing(Bill::getDueDate)).collect(Collectors.toList());

        return outStandingBillList;
    }
}