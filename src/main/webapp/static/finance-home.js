function loadFinanceHome(){

	let xhttp = new XMLHttpRequest();
	let div = document.getElementById('infof');
	xhttp.onload = (resp) => {
		if(xhttp.status === 200){
			let userObj = JSON.parse(xhttp.responseText);
			console.log(userObj);
			div.innerText = `
			Name: ${userObj.firstName} ${userObj.lastName} 
			Email: ${userObj.email} 
			Role: ${userObj.roleId.userRole}
			`
		}else{
			window.location = './login.html';
		}
	}
	
	xhttp.open('GET', '../users/finance-home');
	xhttp.send();
}
