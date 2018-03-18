<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
		mysqli_set_charset($this->conn , "utf8");
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name,$lname,$email,$sex,$age,$password) {

        $stmt = $this->conn->prepare("INSERT INTO `consumer`(`fname`, `lname`, `email`, `sex`, `age`,`password`) VALUES (?,?,?,?,?,?)");
        $stmt->bind_param("ssssss", $name, $lname, $email, $sex, $age ,$password);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM `consumer` WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM `consumer` WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

			$password_in_db = $user['password'];
            // check for password equality
            if ($password == $password_in_db) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from `consumer` WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
	public function getDetailsUser($email){
		$stmt = $this->conn->prepare("SELECT * from `consumer` WHERE email = ?");
		$stmt->bind_param("s", $email);
		if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;

	}

}

	public function getDetail_buffet_type($type_id){
		
	$sql= "SELECT title , rating ,image  , pro_id FROM promotion WHERE type_buffet = $type_id";
	$result = mysqli_query( $this->conn ,$sql);
	if(mysqli_num_rows($result)){
		
		while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
		
	}
	return $array;
	}
	else{
		return ;
	}

	
	}
	
	public function getDetail_price_type($type_id){
		
	$sql= "SELECT title , rating ,image ,pro_id FROM promotion WHERE type_price = $type_id";
	$result = mysqli_query( $this->conn ,$sql);
	if(mysqli_num_rows($result)){
		
		while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
		
	}
	return $array;
	}
	else{
		return ;
	}

	
	
	}
	
	
	public function getbuffet_type(){
		
	$sql= "SELECT * FROM `type_buffet`";
	$result = mysqli_query( $this->conn ,$sql);
	
	while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
	
		
	}
	return $array;
	
	}
	
	public function getprice_type(){
		
	$sql= "SELECT * FROM `type_price`";
	$result = mysqli_query( $this->conn ,$sql);
	
	while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
	
		
	}
	return $array;
	
	}
	
	public function getdetail_room($roomid){
		
	$sql= "SELECT room.room_id , room.title , promotion.des , promotion.location , DATE(room.t_start) AS date , TIME(room.t_start) AS time , promotion.sp_cdt FROM promotion , room WHERE room.room_id = $roomid AND promotion.pro_id = room.pro_id";
	$result = mysqli_query( $this->conn ,$sql);
	
	while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
	
		
	}
	return $array;
	
	}
	
	
	public function getlist_room(){
		
	$sql= "SELECT p.image , p.title AS pro_title , r.title AS room_title FROM promotion p , room r WHERE p.pro_id = r.pro_id ORDER BY r.room_id DESC LIMIT 10";
	$result = mysqli_query( $this->conn ,$sql);
	
	while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
	
		
	}
	return $array;
	
	}
	
	
	public function getuserinroom($roomid){
	$sql= "SELECT list_consumer.user_id , consumer.fname , consumer.lname FROM list_consumer , consumer WHERE list_consumer.user_id = consumer.user_id AND list_consumer.room_id = $roomid";
	$result = mysqli_query( $this->conn ,$sql);
	
	while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
	
		
	}
	return $array;
	
	}
	
	
	public function getpro_from_proid($pro_id){
	$sql= "SELECT * FROM promotion p, type_price tp WHERE p.pro_id = $pro_id";
	$result = mysqli_query( $this->conn ,$sql);
	
	while ($row = mysqli_fetch_assoc($result)) {
		
		$array[] = $row;
	
		
	}
	
	return $array;
	
	}
	
	
	public function insertRoom($pro_id,$user_header,$title,$des,$people_max,$t_start) {
						
						
		$stmt = $this->conn->prepare("INSERT INTO room(pro_id,user_header,`title`,`des`,people_max,`t_start`) VALUES(?,(SELECT user_id AS user_header FROM consumer WHERE email = ?),?,?,?,(SELECT STR_TO_DATE(?,'%Y-%m-%dT%H:%i')));");
		
        $stmt->bind_param("isssis", $pro_id,$user_header,$title,$des,$people_max,$t_start);		
				          
			 if ($stmt->execute()) {
            $user = $stmt->insert_id;		
            $stmt->close();
			return $user;
			 } else {
				return false;
			 }       
	
}
}
?>
