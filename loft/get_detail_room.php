<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
		$title = $_POST['title'];
        $result = $db->getDetailRoom($title);	
        echo json_encode($result, JSON_UNESCAPED_UNICODE);
		

?>

