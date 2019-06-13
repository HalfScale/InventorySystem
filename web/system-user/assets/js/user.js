var userTable = document.getElementById('user-table');
var userTableTbody = document.querySelector('#user-table tbody');
var userTableRow = document.querySelector('.user-row');
var userModalCloseBttn = document.querySelector('.user-modal-close');

var responseDialog = document.getElementById('response-dialog');
var responseText = document.querySelector('.response-dialog-text');
var responseConfirmBttn = document.querySelector('.response-dialog-confirm-bttn');

var addUserBttn = document.getElementById('add-user-item-span');

var request = new XMLHttpRequest();
var url = '/InventorySystem/SystemController';
var param = '?command=' + 'LIST_USER';

getRolesForSelect();

request.open('GET', url + param, true);
request.onreadystatechange = function() {
    if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
        console.log('result', JSON.parse(this.responseText));
        
        var result = JSON.parse(this.responseText);
        
        result.forEach(function(item) {
            
            var row = document.createElement('tr');
//            row.dataset['userId'] = item.id;
//            row.dataset['roleId'] = item.role.id;
//            row.classList.add('clicky');
//            row.classList.add('user-row');

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

userModalCloseBttn.onclick = function() {
    userFormDiv.style.display = 'none';
}

var userFormDiv = document.getElementById('user-form-div');
var userForm = document.getElementById('user-form');
var roleSelect = document.querySelector('.select-role');

addUserBttn.onclick = function() {
    userFormDiv.style.display = 'block';
}

var username = document.getElementById('user-username');
var fullname = document.getElementById('user-name');
var password = document.getElementById('user-password');
var passwordRe = document.getElementById('user-re-password');
var role = document.getElementById('user-select-role');

var fields = [username, fullname, password, passwordRe, role];

userForm.onsubmit = function(e) {
    event.preventDefault();
    console.log('Form submitted prevents default');
    
    if(password.value === passwordRe.value) {
        var fd = {
            username: username.value,
            name: fullname.value,
            password: password.value,
            passwordRe: passwordRe.value,
            role: role.value,
            roleLabel: role.options[role.selectedIndex].innerHTML
        }
        
         var request = new XMLHttpRequest();
         var url = '/InventorySystem/SystemController';
         var params = 'command=ADD_USER&params=' + JSON.stringify(fd);
         request.open('POST', url, true);
         request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
         
         request.onreadystatechange = function() {
             if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                 console.log('user submit', this.responseText);
                 userFormDiv.style.display = 'none';
                 
                 clearFormFields(fields);
                 responseMessage(this.responseText);
                 createRow(fd);
             }
         }
         
         request.send(params);
    }
    
}

responseConfirmBttn.onclick = function() {
    responseDialog.style.display = 'none';
}

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

function getRolesForSelect() {
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemQuery';
    var param = '?params=' + 'role';

    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function () {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
            console.log('role result', result);
            
            addOptionFiller(roleSelect, 'Select a role');
            addOption(roleSelect, result);

        }
    }

    xmlhttp.send();
}

function responseMessage(text) {
    responseDialog.style.display = 'block';
    responseText.innerHTML = text;
    
}

function createRow(item) {
    var row = document.createElement('tr');
//    row.dataset['userId'] = item.id;
//    row.dataset['roleId'] = item.role.id;
//    row.classList.add('clicky');
//    row.classList.add('user-row');

    var columns = [item.name, item.username, item.roleLabel];

    columns.forEach(function(column) {
       var cell = document.createElement('td'); 
       cell.innerHTML = column;
       row.appendChild(cell);
    });

    userTableTbody.appendChild(row);
}

function clearFormFields(fields) {
    fields.forEach(function(field) {
        field.value = "";
    })
}