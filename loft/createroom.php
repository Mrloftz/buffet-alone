<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */
if($_SERVER["REQUEST_METHOD"]=="POST"){
require_once 'include/DB_Connect.php';
    createroom();
}
 function createroom(){
	global $connect;
	
    $pro_id = $_POST['pro_id'];
	$user_header = $_POST['user_header'];
	$des = $_POST['des'];
    $people = $_POST['people'];
	$people_max = $_POST['people_max'];
	$t_start = $_POST['t_start'];
    $t_end = $_POST['t_end'];

	$query="Insert into room(pro_id,user_header,des,people,people_max,t_start,t_end)value($pro_id,$user_header,$des,$people,$people_max,t_start,t_end)";
	mysqli_query($connect,$query)or die(mysqli_error($connect));
	mysqli_close($connect);
}
?>




