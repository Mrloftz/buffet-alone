<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email'])) {

    // receiving the post params

    $email = $_POST['email'];

        $user = $db->getDetailsUser($email);
        if ($user) {
			$response["error"] = FALSE;
            $response["user"]["fname"] = $user["fname"];
			$response["user"]["lname"] = $user["lname"];
            $response["user"]["email"] = $user["email"];
			$response["user"]["sex"] = $user["sex"];
			$response["user"]["age"] = $user["age"];
			$response["user"]["value_1"] = $user["punctuality"];
			$response["user"]["value_2"] = $user["polite"];
			$response["user"]["value_3"] = $user["reliability"];
			
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }

} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters is missing!";
    echo json_encode($response);
}
?>

