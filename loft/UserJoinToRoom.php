<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['roomid']) && isset($_POST['email'])) {

    // receiving the post params
	$roomid = $_POST['roomid'];
	$email = $_POST['email'];



        // create a new user
        $result = $db->UserJoinToRoom($roomid,$email);		
        if ($result) {
            // user stored successfully
            $response["error"] = false;
			$response["room_id"] = $result;
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>

