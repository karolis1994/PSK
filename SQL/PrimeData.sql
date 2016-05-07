-- Test user
INSERT INTO principals (email, points, firstname, lastname, isadmin, isapproved, isdeleted, passwordhash, salt)
    VALUES ('a@a.a', 999, 'a', 'a', TRUE, TRUE, FALSE, -1, '');

-- Yearly member's fee
INSERT INTO otherservices (title, description)
    VALUES ('Metinis nario mokestis', 'Mokestis kurį turi sumokėti kiekvienas narys.');

INSERT INTO paidservices (cost, costinpoints, otherserviceid)
    VALUES (10, 10, (SELECT ID FROM otherservices WHERE title = 'Metinis nario mokestis' LIMIT 1));

--User Settings
INSERT INTO Settings (settingname, settingvalue)
VALUES ('MembershipCost','10');

INSERT INTO Settings (settingname, settingvalue)
VALUES ('MaxUserAmmount','100');

--Recommendation Settings
INSERT INTO Settings (settingname, settingvalue)
VALUES ('MinRecommendations','2');

INSERT INTO Settings (settingname, settingvalue)
VALUES ('MaxRecommendations','5');