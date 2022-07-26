#Mail-Solution

This is a SpringBoot Project, having two implementations of mailSender bean:-
1. SpringJavaMailService
   This service is using JavaMailSender bean to send the mail, and is configured using the BeanConfig.java.
2. SimpleJavaMailService
    This service is using JavaMail api to send mail, as expected in the solution.

P.S: Both beans are implementing MailService interface.

Looks like Google SMTP with email/password does not work now,

![](https://media.giphy.com/media/aOften89vRbG/giphy.gif)

So I have used outlook.com for SMTP.
These are my personal credentials which will be updated after a week. 

Also, I have used thymeleaf templates to store the template information and both mail-services are capable to sending HTML.

I have written some testCases (total: 6) using both the services. One positive, One negative and last one loads the data from csv and can work on multiple attributes as well.

Scope of Improvement:-
1. Templates can be better.
2. send method can be async and return Future.