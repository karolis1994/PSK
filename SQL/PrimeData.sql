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

--Houses
INSERT INTO Houses (id, title, description, address, isclosed, isdeleted, capacity)
VALUES (1, 'Vila "Genutės vasara"', 
        'Erdvus ir ištaigingas vasarnamis su dideliu kiemu ir nuostabiu vaizdu į pievą',
        'Vasaros g. 69', false, false, 6);

--Extras
INSERT INTO Extras (id, houseid, title, description, isdeleted)
VALUES (1, 1, 'Valčių nuoma', 
        'Romantiškai paplaukiokite mūsų nuostabiose medinėse valtyse', false);

INSERT INTO Extras (id, houseid, title, description, isdeleted)
VALUES (2, 1, 'Pirtis', 
        'Romantiškai išprakaituokite mūsų nuostabioje suomiškoje pirtyje', false);

--Paid Services
INSERT INTO PaidServices (houseid, cost, costinpoints)
VALUES (1, 50, 50);

INSERT INTO PaidServices (extrasid, cost, costinpoints)
VALUES (1, 10, 10);

INSERT INTO PaidServices (extrasid, cost, costinpoints)
VALUES (2, 10, 10);



