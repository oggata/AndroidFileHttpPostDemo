<?php

$file       = $_FILES['file']['tmp_name'];
$fileName   = $_FILES['file']['name'];
$fileType   = $_FILES['file']['type'];

$rootName = reset(explode(".", $fileName));
$extension = end(explode(".", $fileName));

// create new file name
$time = time();
$newName = $rootName.$time.'.'.$extension;

$fileName = $_POST['fileName'];
$body = $_POST['body'];
$userId = $_POST['userId'];

// temporarily save file
$moved = move_uploaded_file($_FILES["file"]["tmp_name"], "/var/www/html/saved/".$fileName);
if ($moved) $path = "uploads/".$newName;

$time = time();
if ($moved) {
    $fullUrl = "http://localhost/saved/".$path;
    $arrayToSend = array('status'=>'success','time'=>$time,'body'=>$body,'userId'=>$userId, "imageURL"=>$fullUrl);
} else {
    $arrayToSend = array('status'=>'FAILED','time'=>$time,'body'=>$body,'userId'=>$userId);
}

header('Content-Type:application/json');
echo json_encode($arrayToSend);

?>
