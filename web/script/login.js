var loginSubmit = document.getElementById('login-form');
var statusBar = document.querySelector('.status-bar');
var statusResponse = document.querySelector('.status-response');

loginSubmit.onsubmit = function(event) {
    event.preventDefault();
    console.log('submitted!');
    
    var user = document.getElementById('user');
    var pass = document.getElementById('pass');
    
    
    statusBar.style.display = 'block';
    statusResponse.innerHTML = 'Verifying username and password...';
    
    var request = new XMLHttpRequest();
    var url = '/InventorySystem/SystemLogin';
    var param = 'username=' + user.value + '&password=' + pass.value;
    request.open('POST', url, true);
    request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    
    request.onreadystatechange = function() {
        var result;
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            result = JSON.parse(this.responseText);
            console.log('response', result);
            console.log('response success', this.responseText);
            
            statusResponse.innerHTML = result.message;
            if(result.status === 0) {
                window.location.href = '/InventorySystem/system-home/home.jsp';
            }
            
        }
    }
    
    request.send(param);
}