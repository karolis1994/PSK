-- Default profile picture
INSERT INTO Pictures (ImageName, Image)
	VALUES ('Default_profile_picture.png', '\211PNG\015\012\032\012\000\000\000\015IHDR\000\000\002\000\000\000\002\000\004\003\000\000\000\006V\311\311\000\000\000\004gAMA\000\000\261\217\013\374a\005\000\000\000!PLTE\347\347\347\332\332\332\333\332\333\350\350\350\353\353\353\351\351\351\352\352\3 (...)');

-- Yearly member's fee
INSERT INTO otherservices (title, description)
    VALUES ('Metinis nario mokestis', 'Mokestis kurį turi sumokėti kiekvienas narys.');

INSERT INTO paidservices (cost, costinpoints, otherserviceid)
    VALUES (10, 10, (SELECT ID FROM otherservices WHERE title = 'Metinis nario mokestis' LIMIT 1));

-- Buy points
INSERT INTO otherservices (title, description)
    VALUES ('Taškų pirkimas', '');

INSERT INTO paidservices (cost, costinpoints, otherserviceid)
    VALUES (1, 1, (SELECT ID FROM otherservices WHERE title = 'Taškų pirkimas' LIMIT 1));

--User Settings
INSERT INTO Settings (settingname, settingvalue)
VALUES ('MaxUserAmmount','100');

--Recommendation Settings
INSERT INTO Settings (settingname, settingvalue)
VALUES ('MinRecommendations','2');

INSERT INTO Settings (settingname, settingvalue)
VALUES ('MaxRecommendations','5');

--Registration form Settings
INSERT INTO Settings (settingname, settingvalue)
VALUES ('AboutField', 'false');

INSERT INTO Settings (settingname, settingvalue)
VALUES ('PictureField', 'false');

--Houses
INSERT INTO Houses (id, title, description, address, isclosed, isdeleted, capacity, availablefrom, availableto)
VALUES (1, 'Vila "Genutės vasara"', 
        'Erdvus ir ištaigingas vasarnamis su dideliu kiemu ir nuostabiu vaizdu į pievą',
        'Vasaros g. 69', false, false, 6, 7, 8);

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



