var modal = document.getElementById("form-modal-container");
var addItemBttn = document.getElementsByClassName("add-item-span")[0];
var modalCloseBttn = document.getElementsByClassName("close")[0];
var addItemForm = document.getElementById("add-item-form");

addItemBttn.onclick = function() {
    modal.style.display = "block";
};

modalCloseBttn.onclick = function() {
    modal.style.display = "none";
    clearFormInputs();
    
};

addItemForm.onsubmit = function(event) {
    event.preventDefault();
    
    var name = document.getElementById("name");
    var code = document.getElementById("code");
    var description = document.getElementById("description");
    var price = document.getElementById("price");
    var stock = document.getElementById("stock");
    
    var data = {
        name: name.value,
        code: code.value,
        description: description.value,
        price: price.value,
        stock: stock.value
    };
    
    var xmlhttp = new XMLHttpRequest();
    var param = 'command=ADD&param=' + JSON.stringify(data);
    var url = '/InventorySystem/SystemController';
    xmlhttp.open('POST', url, true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log(this.responseText);
            var tableBody = document.getElementsByClassName('table-body')[0];
            
            //Forming a table row for the new inserted item
            var tableRow = document.createElement('tr');
            
            for (var key in data) {
                var tableCell = document.createElement('td');
                tableCell.innerHTML = data[key];
                tableRow.appendChild(tableCell);
            }
            
            tableBody.appendChild(tableRow);
            modalCloseBttn.click();
        }
    };
    
    xmlhttp.send(param);
};

//<editor-fold defaultstate="expanded" desc="code for displaying item">
var xmlhttp = new XMLHttpRequest();
var param = '?command=LIST';
var url = '/InventorySystem/SystemController';
xmlhttp.open('GET', url + param, true);

xmlhttp.onreadystatechange = function() {
    if(this.readyState === XMLHttpRequest.DONE && this.status === 200){
        var tableBody = document.getElementsByClassName('table-body')[0];
        var result = JSON.parse(this.responseText);
        console.log('result', result);
        
        result.forEach(function(item){
            var tableRow = document.createElement('tr');
            
            for(var key in item) {
                //ommit the id field here
                if(key != 'id') {
                    var tableCell = document.createElement('td');
                    tableCell.innerHTML = item[key];
                    tableRow.appendChild(tableCell);
                }
            }
            
            tableBody.appendChild(tableRow);
        });
    }
};

xmlhttp.send();
//</editor-fold>

/*
 * used for modals
 */
function clearFormInputs() {
    var name = document.getElementById("name");
    var code = document.getElementById("code");
    var description = document.getElementById("description");
    var price = document.getElementById("price");
    var stock = document.getElementById("stock");
    
    name.value = "";
    code.value = "";
    description.value = "";
    price.value = "";
    stock.value = "";
    
}