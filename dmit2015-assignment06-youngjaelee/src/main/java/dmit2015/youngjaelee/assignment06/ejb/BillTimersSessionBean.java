package dmit2015.youngjaelee.assignment06.ejb;


import dmit2015.youngjaelee.assignment06.entity.Bill;
import dmit2015.youngjaelee.assignment06.repository.BillRepository;
import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.logging.Logger;

@Singleton
@Startup
public class BillTimersSessionBean {
    @Resource   // This is a container created resource
    private TimerService _timerService;

    private Logger _logger = Logger.getLogger(BillTimersSessionBean.class.getName());

    @Inject
    private BillRepository _billRepository;

    @Inject
    @ConfigProperty(name="dmit2015.youngjaelee.MailToAddresses")
    private String mailToAddress;

    @Inject
    private EmailSessionBean mail;

    @Schedule(second = "0", minute ="0", hour = "6", dayOfWeek = "Mon-Fri", month = "*", year = "2022", info ="DMIT2015-OE01 Assignment 6", persistent = false)
    public void dmit2015SectionOE01ClassNotifiation(Timer timer) {
        try {
            sendEmail();

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            _logger.fine(errorMessage);
        }
    }



    private void sendEmail() {
        if (!mailToAddress.isBlank()) {
            String mailSubject = "Outstanding bills (Must be paid in 3 days)";
            String mailText = null;

            List<Bill> outstandingBillList = _billRepository.getOutstandingbillsWithin3Days();

            String finalString = "";

            for (Bill item : outstandingBillList) {
                finalString += "<tr>\n" +
                        "<td style=\"border: 1px solid black; border-collapse: collapse;\">" + item.getPayeeName() + "</td>\n" +
                        "<td style=\"border: 1px solid black; border-collapse: collapse;\">" + item.getAmountBalance() + "</td>\n" +
                        "<td style=\"border: 1px solid black; border-collapse: collapse;text-align: right;\">" + item.getDueDate() + "</td>\n" +
                        "</tr>";
            }

            mailSubject = "DMIT2015 Assignment 6 - Outstanding bills within 3 days";
            mailText = "<table style=\"border: 1px solid black; border-collapse: collapse;\">\n" +
                    "  <tr>\n" +
                    "    <th style=\"border: 1px solid black; border-collapse: collapse;\">Payee Name</th>\n" +
                    "    <th style=\"border: 1px solid black; border-collapse: collapse;\">Balance Due</th>\n" +
                    "    <th style=\"border: 1px solid black; border-collapse: collapse; text-align: right;\">Due Date</th>\n" +
                    "  </tr>" + finalString + "</table>";


            try {
                mail.sendHtmlEmail(mailToAddress, mailSubject, mailText);
                _logger.info("Successfully sent email to " + mailToAddress);
            } catch (Exception e) {
                e.printStackTrace();
                _logger.fine("Error sending email with exception " + e.getMessage());
            }
        }
    }
}
