var addModal = document.querySelector('.add-item-modal');
var updateModal = document.querySelector('.update-item-modal');
var historyModal = document.querySelector('.history-item-modal');
var archiveModal = document.querySelector('.archive-item-modal');
var brandModal = document.querySelector('.brand-item-modal');

var brandTableBody = document.querySelector('.item-brand-tbody');

var addItemBttn = document.getElementById('add-item-span');
var archiveItemBttn = document.getElementById('archive-item-span');
var brandItemBttn = document.getElementById('brand-item-span');

var addCloseBttn = document.querySelector('.add-modal-close');
var updateCloseBttn = document.querySelector('.update-modal-close');
var historyCloseBttn = document.querySelector('.history-modal-close');
var archiveCloseBttn = document.querySelector('.archive-modal-close');
var brandCloseBttn = document.querySelector('.brand-modal-close');

var addItemForm = document.getElementById('add-item-form');
var updateItemForm = document.getElementById('update-item-form');

addItemBttn.onclick = function() {
    console.log('click!');
    addModal.style.display = 'block';
};

addCloseBttn.onclick = function() {
    addModal.style.display = 'none';
};

updateCloseBttn.onclick = function() {
    updateModal.style.display = 'none';
};

historyCloseBttn.onclick = function() {
    historyModal.style.display = 'none';
};

archiveCloseBttn.onclick = function() {
    archiveModal.style.display = 'none';
};

brandCloseBttn.onclick = function() {
    brandModal.style.display = 'none';
    removeElemChild(brandTableBody);
}

addItemForm.onsubmit = function(event) {
    event.preventDefault();
    
    var name = document.getElementById("add-name");
    var code = document.getElementById("add-code");
    var description = document.getElementById("add-description");
    var price = document.getElementById("add-price");
    var resellerPrice = document.getElementById("add-reseller-price");
    var stock = document.getElementById("add-stock");
    
    var data = {
        name: name.value,
        code: code.value,
        description: description.value,
        price: price.value,
        resellerPrice: resellerPrice.value,
        stock: stock.value
    };
    
    var xmlhttp = new XMLHttpRequest();
    var param = 'command=ADD&param=' + JSON.stringify(data);
    var url = '/InventorySystem/SystemController';
    xmlhttp.open('POST', url, true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            window.location.reload();
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
        
        var excludedRows = ['id'];
        
        result.forEach(function(item) {
            var tableRow = document.createElement('tr');
            tableRow.className = 'item-row';
            tableRow.dataset.itemId = item.id;
            
            for(var key in item) {
                //ommit the id field here
                if(!excludedRows.includes(key)) {
                    var tableCell = document.createElement('td');
                    tableCell.innerHTML = item[key];
                    
                    if (key === 'price' || key === 'resellerPrice') {
                        //price should have 2 decimal places
                        tableCell.innerHTML = parseFloat(item[key]).toFixed(2);
                    }
                    
                    tableRow.appendChild(tableCell);
                }
                
                
            }
            
            tableBody.appendChild(tableRow);
        });
    }
};

xmlhttp.send();
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="code for updating item">

//Update item table row clicky (modal pop up)
var itemRow = document.querySelector('.table-body');
itemRow.addEventListener('click', function(event){
    console.log(event.target.parentNode);
    var row = event.target.parentNode;
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var itemId = row.dataset.itemId;
    console.log('itemId',itemId);
    var param = '?itemId=' + itemId + '&command=LOAD';
    
    xmlhttp.open('GET', url + param, true);
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log(this.responseText);
            var item = JSON.parse(this.responseText);
            
            var id = document.getElementById("update-id");
            var name = document.getElementById("update-name");
            var code = document.getElementById("update-code");
            var description = document.getElementById("update-description");
            var price = document.getElementById("update-price");
            var resellerPrice = document.getElementById("update-reseller-price");
            var stock = document.getElementById("update-stock");
            
            id.value = item.id;
            name.value = item.name;
            code.value = item.code;
            description.value = item.description;
            price.value = parseFloat(item.price).toFixed(2);
            resellerPrice.value = parseFloat(item.resellerPrice).toFixed(2);
            stock.value = item.stock;
            
            updateModal.style.display = "block";
        }
    };
    
    xmlhttp.send();
    
});

updateItemForm.onsubmit = function(event) {
    event.preventDefault();
    
    var id = document.getElementById("update-id");
    var name = document.getElementById("update-name");
    var code = document.getElementById("update-code");
    var description = document.getElementById("update-description");
    var price = document.getElementById("update-price");
    var resellerPrice = document.getElementById("update-reseller-price");
    var stock = document.getElementById("update-stock");
    
    var data = {
        id: id.value,
        name: name.value,
        code: code.value,
        description: description.value,
        price: toTwoDecimal(price.value),
        resellerPrice: toTwoDecimal(resellerPrice.value),
        stock: stock.value
    };
    
    //the arrangement of this array is the same
    // with the arrangement of the table columns
    var dataArray = [data.name, data.code, data.description, data.price, data.resellerPrice, data.stock];
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = 'command=UPDATE' + '&param=' + JSON.stringify(data);
    console.log('param', param);
    xmlhttp.open('POST', url, true);
    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var tableBody = document.querySelector('.table-body');
            
            //idk why the fck i need to parse this.
            //It doesn't go through the if statement if not parsed :/
            var targetRowId = parseInt(this.responseText); 

            for (var i = 0, row; row = tableBody.rows[i] ; i++) {
                if (targetRowId == row.dataset.itemId) {
                    
                    for (var i = 0, cell; cell = row.cells[i]; i++) {
                        cell.innerHTML = dataArray[i];
                    }
                    
                    updateCloseBttn.click();
                    break;
                }
            }
        }
    };
    
    xmlhttp.send(param);
};

//For item deletion
var deleteItemBttn = document.querySelector('.delete-item-button');
deleteItemBttn.onclick = function(event) {
    event.preventDefault();
    var id = document.getElementById("update-id");
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?itemId=' + id.value + '&command=DELETE';
    xmlhttp.open('GET', url + param, true);
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var tableBody = document.querySelector('.table-body');
            
            //idk why the fck i need to parse this.
            //It doesn't go through the if statement if not parsed :/
            var targetRowId = parseFloat(this.responseText);
            
            for (var i = 0, row; row = tableBody.rows[i]; i++) {
                console.log('targetRowId', targetRowId);
                console.log('dataset', row.dataset.itemId);
                if(targetRowId == row.dataset.itemId) {
                    console.log('target found!');
                    tableBody.removeChild(row);
                    break;
                }
            }
            
            updateCloseBttn.click();
        }
    };
    
    xmlhttp.send();
}

//For item history viewing
var historyItemBttn = document.querySelector('.history-item-button');
var tBody = document.querySelector('.item-history-tbody');
historyItemBttn.onclick = function(event) {
    event.preventDefault();
    var id = document.getElementById("update-id");
    console.log('clicked!', id.value);  
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?itemId=' + id.value + '&command=HISTORY';
    xmlhttp.open('GET', url + param, true);
    
    //This is a dummy row
    tBody.appendChild(createDummyRow('Loading...', 8));
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            removeElemChild(tBody);
            
            var result = JSON.parse(this.responseText);
            var excludedCol = ['id', 'itemId'];
            
            console.log('result', result);
            
            
            if (result.length === 0) {
                tBody.appendChild(createDummyRow('No result...', 8));
            }
            
            result.forEach(function(item){
                var tableRow = document.createElement('tr');
                
                for(var key in item) {
                    
                    if(!excludedCol.includes(key)){
                        var tableCell = document.createElement('td');
                        tableCell.innerHTML = item[key];
                        
                        if (key === 'price' || key === 'resellerPrice') {
                            //price should have 2 decimal places
                            tableCell.innerHTML = parseFloat(item[key]).toFixed(2);
                        }
                    
                        tableRow.appendChild(tableCell);
                    }
                }
                
                tBody.appendChild(tableRow);
            });
            
            historyModal.style.display = 'block';
        }
    };
    
    xmlhttp.send();
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="code for searching items">
var searchBar = document.getElementById('item-search-bar');
searchBar.onkeyup = function() {
    var input, filter, table, tr, td, textValue;
    input = searchBar;
    filter = input.value.toLowerCase();
    table = document.getElementById('item-table');
    tr = table.getElementsByTagName('tr');
    
    for (var i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName('td')[0]; // refers to the name column of the item table
        
        if (td) {
            textValue = td.textContent || td.innerText;
            console.log('textValue', textValue.toLowerCase());
            //if it doesn't match the word then display none.
            if (textValue.toLowerCase().indexOf(filter) > -1) {
                tr[i].style.display = '';
                
            }else {
                tr[i].style.display = 'none';
            }
        }
    }
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="code for displaying item archive">
var archiveTbody = document.querySelector('.item-archive-tbody');
archiveItemBttn.onclick = function() {
    archiveModal.style.display = 'block';
    
    var xmlhttp = new XMLHttpRequest();
    var param = '?command=ARCHIVE';
    var url = '/InventorySystem/SystemController';
    xmlhttp.open('GET', url + param, true);
    removeElemChild(archiveTbody);
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('response', this.responseText);
            var tableBody = document.querySelector('.item-archive-tbody');
            var result = JSON.parse(this.responseText);
            
            var excludedRows = ['id', 'itemId', 'action'];
            
            result.forEach(function(item) {
                var tableRow = document.createElement('tr');
                tableRow.className = 'item-row';
                
                for(var key in item) {
                    var tableCell = document.createElement('td');
                    
                    if (!excludedRows.includes(key)) {
                        tableCell.innerHTML = item[key];

                        if(key === 'price' || key === 'resellerPrice') {
                            tableCell.innerHTML = parseFloat(item[key]).toFixed(2);
                        }

                        tableRow.appendChild(tableCell);
                    }
                }
                
                tableBody.appendChild(tableRow);
            });
            
        }
    };
    
    xmlhttp.send();
};

//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="code for item brands">
var addItemBrandBttn = document.getElementById('brand-item-add');
var brandInput = document.querySelector('.brand-item-input');

brandItemBttn.onclick = function() {
    brandModal.style.display = 'block';
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_BRAND';
    xmlhttp.open('GET', url + param, true);
    
    xmlhttp.onreadystatechange = function() {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('result', this.responseText);
            
            var brands = JSON.parse(this.responseText);
            
            brands.forEach(function(brand) {
                var row = document.createElement('tr');
                var cell = document.createElement('td');
                cell.innerHTML = brand.name;
                
                row.appendChild(cell);
                
                brandTableBody.appendChild(row);
            });
        }
    }
    
    xmlhttp.send();
}

addItemBrandBttn.onclick = function() {
    console.log('input value', brandInput.value);
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = 'command=ADD_BRAND&param=' + brandInput.value;
    xmlhttp.open('POST', url, true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('result', this.responseText);
            var brandName = this.responseText;
            var row = document.createElement('tr');
            
            var cell = document.createElement('td');
            cell.innerHTML = brandName;
            
            row.appendChild(cell);
            brandTableBody.appendChild(row);
        }
    }
    
    xmlhttp.send(param);
}
//</editor-fold>


/*
 * used for modals
 */

function clearFormInputs(prefix) {
    var name = document.getElementById(prefix + '-' + 'name');
    var code = document.getElementById(prefix + '-' + 'code');
    var description = document.getElementById(prefix + '-' +'description');
    var price = document.getElementById(prefix + '-' + 'price');
    var stock = document.getElementById(prefix + '-' + 'stock');
    
    name.value = "";
    code.value = "";
    description.value = "";
    price.value = "";
    stock.value = "";
}

function findParentClassName(element, targetParent) {
    while(element.parentNode) {
        element = element.parentNode;
        
        //To stop the loop for reaching the unexisting parent
        // causing undefined errors
        if(element.tagName == null){
            break;
        }
        
        var isFound = element.classList.contains(targetParent);
        
        console.log('isFound?', isFound, 'element->', element);
        if (isFound) {
            console.log('target element found!');
            return true;
        }
    }
    
    return null;
}

function removeElemChild(elem) {
    while(elem.lastChild) {
        elem.removeChild(elem.lastChild);
    }
    
    return elem;
}

function createDummyRow(text, colspan) {
    var dummyRow = document.createElement('tr');
    var dummyCell = document.createElement('td');
    dummyCell.setAttribute("colspan", colspan);
    dummyCell.innerHTML = text;
    dummyRow.appendChild(dummyCell);
    
    return dummyRow;
}