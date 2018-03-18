<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['pro_id']) && isset($_POST['user_header']) && isset($_POST['title']) && isset($_POST['des'])  && isset($_POST['people_max'])  && isset($_POST['t_start'])) {

    // receiving the post params
	$pro_id = $_POST['pro_id'];
	$user_header = $_POST['user_header'];
	$title = $_POST['title'];
	$des = $_POST['des'];
	$people_max = $_POST['people_max'];
	$t_start = $_POST['t_start'];


        // create a new user
        $result = $db->insertRoom($pro_id,$user_header,$title,$des,$people_max,$t_start);		
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

