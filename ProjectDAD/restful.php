<?php
    $hostAddr = "localhost";
    $dbName = "carrental";
    $dbUser = "root";
    $dbPwd = "";
    $dbPort = 3306;

    try {
        $dbPDO = new PDO("mysql:host=$hostAddr;dbname=$dbName", $dbUser, $dbPwd);
        $dbPDO->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    } catch (PDOException $e) {
        echo json_encode(['error' => 'Connection failed: ' . $e->getMessage()]);
        exit;
    }

    header('Content-Type: application/json');
    date_default_timezone_set('Asia/Kuala_Lumpur');

    if (isset($_POST)) {
        $varFN = $_REQUEST["login"];

        if ($varFN == "userlogin") {
            $strQry = "SELECT * FROM users WHERE 1=1";
            $params = array();

            if ($_REQUEST["username"] != "" && $_REQUEST["password"] != "") {
                $strQry .= " AND usrname = :usrname AND pass = :pass";
                $params['usrname'] = $_REQUEST["username"];
                $params['pass'] = $_REQUEST["password"];
            }

            try {
                $stmt = $dbPDO->prepare($strQry);
                $stmt->execute($params);
                $recordSetObj = $stmt->fetchAll(PDO::FETCH_OBJ);
                echo json_encode($recordSetObj);
            } catch (PDOException $e) {
                echo json_encode(['error' => 'Query failed: ' . $e->getMessage()]);
            }
        }

        if ($varFN == "cartable") {
            $strQry = "SELECT * FROM ownercars WHERE 1=1";
            $params = array();

            if ($_REQUEST["status"] != "") {
                $strQry .= " AND Status = :Status";
                $params['Status'] = $_REQUEST["status"];
            }

            try {
                $stmt = $dbPDO->prepare($strQry);
                $stmt->execute($params);
                $recordSetObj = $stmt->fetchAll(PDO::FETCH_OBJ);
                echo json_encode($recordSetObj);
            } catch (PDOException $e) {
                echo json_encode(['error' => 'Query failed: ' . $e->getMessage()]);
            }
        }

        if ($varFN == "booking") {
            $strQry = "UPDATE ownercars SET Status='Pending' WHERE carid=:carid";
            $params = array();
            $params['carid'] = $_REQUEST["id"];
            
            $carid = $_REQUEST["id"];
            $userid = $_REQUEST["userid"];
            $hoursToAdd = isset($_REQUEST["hour"]) ? (int)$_REQUEST["hour"] : 0;

            $currentDateTime = date('Y-m-d H:i:s');
            $until = date("Y-m-d H:i:s", strtotime("+$hoursToAdd hours"));

            $query2 = "INSERT INTO booking(carId, userId, bookStart, bookEnd) VALUES (:carid, :userid, :currentDateTime, :until)";
            $parameter = array(
                'carid' => $carid,
                'userid' => $userid,
                'currentDateTime' => $currentDateTime,
                'until' => $until
            );

            try {
                $stmt = $dbPDO->prepare($strQry);
                $result = $stmt->execute($params);

                $statement = $dbPDO->prepare($query2);
                $result2 = $statement->execute($parameter);

                if ($result && $result2) {
                    echo json_encode(array("status" => "success"));
                } else {
                    echo json_encode(array("status" => "failure"));
                }
            } catch (PDOException $e) {
                echo json_encode(['error' => 'Query failed: ' . $e->getMessage()]);
            }
        }

        if ($varFN == "pending") {
            $strQry = "SELECT * FROM booking JOIN users ON booking.userId=users.id JOIN ownercars ON ownercars.carid = booking.carId WHERE 1=1";
            $params = array();

            if ($_REQUEST["status"] != "") {
                $strQry .= " AND Status = :Status";
                $params['Status'] = $_REQUEST["status"];
            }

            try {
                $stmt = $dbPDO->prepare($strQry);
                $stmt->execute($params);
                $recordSetObj = $stmt->fetchAll(PDO::FETCH_OBJ);
                echo json_encode($recordSetObj);
            } catch (PDOException $e) {
                echo json_encode(['error' => 'Query failed: ' . $e->getMessage()]);
            }
        }

        if ($varFN == "approval") {
            $todo = $_REQUEST["todo"];
            if($todo=="Accept"){
                $strQry = "UPDATE ownercars SET Status='Booked' WHERE carid=:carid";
            }else{
                $strQry = "UPDATE ownercars SET Status='Available' WHERE carid=:carid";
            }
            $params = array();
            $params['carid'] = $_REQUEST["carid"];

            try {
                $stmt = $dbPDO->prepare($strQry);
                $result = $stmt->execute($params);

                if ($result) {
                    echo json_encode(array("status" => "success"));
                } else {
                    echo json_encode(array("status" => "failure"));
                }
            } catch (PDOException $e) {
                echo json_encode(['error' => 'Query failed: ' . $e->getMessage()]);
            }
        }

        if ($varFN == "addcar") {
            $carBrand = $_REQUEST["carbrand"];
            $sitType = $_REQUEST["sitType"];
            $platNo = $_REQUEST["platNo"];
            $pricePerHour = $_REQUEST["pricePerHour"];

            $strQry = "INSERT INTO ownercars (carbrand, sitType, platNo, pricePerHour, Status) VALUES (:carbrand, :sitType, :platNo, :pricePerHour, 'Available')";
            $params = array(
                'carbrand' => $carBrand,
                'sitType' => $sitType,
                'platNo' => $platNo,
                'pricePerHour' => $pricePerHour
            );

            try {
                $stmt = $dbPDO->prepare($strQry);
                $result = $stmt->execute($params);

                if ($result) {
                    echo json_encode(array("status" => "success"));
                } else {
                    echo json_encode(array("status" => "failure"));
                }
            } catch (PDOException $e) {
                echo json_encode(['error' => 'Query failed: ' . $e->getMessage()]);
            }
        }

    } else {
        echo json_encode(['error' => 'Invalid request']);
    }

    echo $date = date('Y-m-d H:i:sss', time());
?>
