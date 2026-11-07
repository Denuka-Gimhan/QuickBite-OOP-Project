# Test checklist (run before every pull request)
Login admin/admin123 on one browser, a customer on a second browser (or private window).
- [ ] M1: sign up; duplicate username/email rejected; login with username AND email; edit profile; admin deletes a user
- [ ] M2: add/edit/delete food; search; sort Low->High and High->Low; mark out of stock (cannot add to cart)
- [ ] M3: add to cart, +/-; apply WELCOME10 (cart >= Rs.1,000); admin changes a price -> cart refreshes with a message
- [ ] M4: place order -> "number N in queue"; admin "Process next order" takes the OLDEST first; cancel only while Pending
- [ ] M5: Preparing order -> assign rider -> Picked up -> Delivered; rider shown to the customer; add/off-duty/delete rider
- [ ] M6: View bill and print; Delivered -> payment Paid; Refund; rate a delivered order once; Reports numbers + CSV
