
var userTable = document.getElementById('user-table');
var userTableTbody = document.querySelector('#user-table tbody');
var userTableRow = document.querySelector('.user-row');

var request = new XMLHttpRequest();
var url = '/InventorySystem/SystemController';
var param = '?command=' + 'LIST_USER';

request.open('GET', url + param, true);
request.onreadystatechange = function() {
    if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
        console.log('result', JSON.parse(this.responseText));
        
        var result = JSON.parse(this.responseText);
        
        result.forEach(function(item) {
            
            var row = document.createElement('tr');
            row.dataset['userId'] = item.id;
            row.classList.add('clicky');
            row.classList.add('user-row');

            var columns = [item.name, item.username, item.role.name];

            columns.forEach(function(column) {
               var cell = document.createElement('td'); 
               cell.innerHTML = column;
               row.appendChild(cell);
            });
            
            userTableTbody.appendChild(row);
        });
        
    }
}
request.send();

userTableTbody.onclick = function(e) {
    var userId = e.target.parentNode.dataset['userId'];
    
    var request = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var params = '?command=LIST_USER_BY_ID&id=' + userId;
    
    request.open('GET', url + params, true);
    request.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
            console.log('user by id', result);
        }
    }
    
    request.send();
};

//<editor-fold defaultstate="collapsed" desc="code for role management">
var roleManagementBttn = document.getElementById('role-item-span');
var roleModal = document.getElementById('role-modal');
var roleModalCloseBttn = document.querySelector('.role-modal-close');
var roleTableBody = document.querySelector('#role-table tbody');

roleManagementBttn.onclick = function() {
    roleModal.style.display = 'block';
    
    var request = new XMLHttpRequest();
    var url = '/InventorySystem/SystemQuery';
    var params = '?params=role';
    request.open('GET', url + params, true);
    request.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
//            console.log('result', result);

            result.forEach(function(item) {
                var row = document.createElement('tr');
                row.dataset['roleId'] = item.id;
                row.classList.add('clicky');
                
                var cell = document.createElement('td');
                cell.innerHTML = item.name;
                
                row.appendChild(cell);
                roleTableBody.append(row);
            });
        }
    }
    
    request.send();
}

roleModalCloseBttn.onclick = function() {
    roleModal.style.display = 'none';
    removeElemChild(roleTableBody);
}
//</editor-fold>
