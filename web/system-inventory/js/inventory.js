var addModal = document.querySelector('.add-item-modal');
var updateModal = document.querySelector('.update-item-modal');
var historyModal = document.querySelector('.history-item-modal');
var archiveModal = document.querySelector('.archive-item-modal');
var brandModal = document.querySelector('.brand-item-modal');
var categoryModal = document.querySelector('.category-item-modal');

var brandDeleteModal = document.getElementById('brand-delete-modal');

var brandTableBody = document.querySelector('.item-brand-tbody');
var categoryTableBody = document.querySelector('.item-category-tbody');

var addItemBttn = document.getElementById('add-item-span');
var archiveItemBttn = document.getElementById('archive-item-span');
var brandItemBttn = document.getElementById('brand-item-span');
var categoryItemBttn = document.getElementById('categories-item-span');

var addCloseBttn = document.querySelector('.add-modal-close');
var updateCloseBttn = document.querySelector('.update-modal-close');
var historyCloseBttn = document.querySelector('.history-modal-close');
var archiveCloseBttn = document.querySelector('.archive-modal-close');
var brandCloseBttn = document.querySelector('.brand-modal-close');
var categoryCloseBttn = document.querySelector('.category-modal-close');

var addItemForm = document.getElementById('add-item-form');
var updateItemForm = document.getElementById('update-item-form');

var addBrandSelect = document.getElementById('add-brand');
var addCategorySelect = document.getElementById('add-category');

var updateBrandSelect = document.getElementById('update-brand');
var updateCategorySelect = document.getElementById('update-category');

addItemBttn.onclick = function() {
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_BRAND';

    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function () {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
            console.log('brand result', result);
            addOptionFiller(addBrandSelect, 'Select a brand');
            addOption(addBrandSelect, result);

        }
    }
    
    xmlhttp.send();

    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_CATEGORY';

    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function () {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
            console.log('category result', result);
            addOptionFiller(addCategorySelect, 'Select a category');
            addOption(addCategorySelect, result);
        }
    }
    
    xmlhttp.send();
    
    addModal.style.display = 'block';
};

addCloseBttn.onclick = function() {
    removeElemChild(addBrandSelect);
    removeElemChild(addCategorySelect);
    addModal.style.display = 'none';
};

updateCloseBttn.onclick = function() {
    removeElemChild(updateBrandSelect);
    removeElemChild(updateCategorySelect);
    updateModal.style.display = 'none';
};

historyCloseBttn.onclick = function() {
    historyModal.style.display = 'none';
};

archiveCloseBttn.onclick = function() {
    archiveModal.style.display = 'none';
};

categoryCloseBttn.onclick = function() {
    categoryModal.style.display = 'none';
    removeElemChild(categoryTableBody);
}

brandCloseBttn.onclick = function() {
    brandModal.style.display = 'none';
    removeElemChild(brandTableBody);
}

addItemForm.onsubmit = function(event) {
    event.preventDefault();
    
    var name = document.getElementById("add-name");
    var code = document.getElementById("add-code");
    var description = document.getElementById("add-description");
    var brand = document.getElementById("add-brand");
    var category = document.getElementById("add-category");
    var price = document.getElementById("add-price");
    var resellerPrice = document.getElementById("add-reseller-price");
    var stock = document.getElementById("add-stock");
    
    var data = {
        name: name.value,
        code: code.value,
        brand: {
            id: brand.value,
            name: brand.options[brand.selectedIndex].text
        },
        category: {
            id: category.value,
            name: category.options[category.selectedIndex].text
        },
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
            tableRow.dataset['itemId'] = item.id;
            
            for(var key in item) {
                //ommit the id field here
                if(!excludedRows.includes(key)) {
                    var tableCell = document.createElement('td');
                    tableCell.classList.add(key);
                    tableCell.innerHTML = item[key];
                    
                    //brand and category are special cases
                    if(key === 'brand' || key === 'category') {
                        tableCell.innerHTML = item[key].name;
                        
                        if(key === 'brand') {
                            tableRow.dataset['brandId'] = item[key].id;
                        }else {
                            tableRow.dataset['categoryId'] = item[key].id;
                        }
                    }
                    
                    //price and resellerPrice needs to be formatted
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
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_BRAND';

    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function () {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
            console.log('brand result', result);
            addOption(updateBrandSelect, result);

        }
    }

    xmlhttp.send();

    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_CATEGORY';

    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function () {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            var result = JSON.parse(this.responseText);
            console.log('category result', result);
            addOption(updateCategorySelect, result);
        }
    }

    xmlhttp.send();
    
    var row = event.target.parentNode;
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var itemId = row.dataset.itemId;
    console.log('itemId',itemId);
    var param = '?itemId=' + itemId + '&command=LOAD';
    
    xmlhttp.open('GET', url + param, true);
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('result for update', this.responseText);
            var item = JSON.parse(this.responseText);
            
            var id = document.getElementById('update-id');
            var name = document.getElementById('update-name');
            var code = document.getElementById('update-code');
            var description = document.getElementById('update-description');
            var price = document.getElementById('update-price');
            var resellerPrice = document.getElementById('update-reseller-price');
            var stock = document.getElementById('update-stock');
            
            id.value = item.id;
            name.value = item.name;
            code.value = item.code;
            updateBrandSelect.value = item.brand.id;
            console.log('category id', item.category.id);
            updateCategorySelect.value = item.category.id;
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
    var brand = document.getElementById("update-brand");
    var category = document.getElementById("update-category");
    var description = document.getElementById("update-description");
    var price = document.getElementById("update-price");
    var resellerPrice = document.getElementById("update-reseller-price");
    var stock = document.getElementById("update-stock");
    
    var data = {
        id: id.value,
        name: name.value,
        code: code.value,
        brand: {
            id: brand.value,
            name: brand.options[brand.selectedIndex].text
        },
        category: {
            id: category.value,
            name: category.options[category.selectedIndex].text
        },
        description: description.value,
        price: toTwoDecimal(price.value),
        resellerPrice: toTwoDecimal(resellerPrice.value),
        stock: stock.value
    };
    
    //the arrangement of this array is the same
    // with the arrangement of the table columns
    var dataArray = [data.name, data.code, {id: data.brand.id, name: data.brand.name}, {id: data.category.id, name: data.category.name}, data.description, data.price, data.resellerPrice, data.stock];
    
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
            console.log('update form result', this.responseText);
            var targetRowId = parseInt(this.responseText); 
            for (var i = 0, row; row = tableBody.rows[i] ; i++) {
                if (targetRowId == row.dataset.itemId) {
                    
                    for (var i = 0, cell; cell = row.cells[i]; i++) {
                        console.log('innerHTML', cell.innerHTML );
                        if(cell.classList.contains('brand') || cell.classList.contains('category')) {
                            console.log('brand or category', dataArray[i].name);
                            cell.innerHTML = dataArray[i].name;
                        }else {
                            cell.innerHTML = dataArray[i];
                        }
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
var brandForm = document.getElementById('brand-form');
var brandInput = document.querySelector('.brand-item-input');
console.log('');
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

brandForm.onsubmit = function(event) {
    event.preventDefault();
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
            brandInput.value = '';
            brandInput.focus();
        }
    }
    
    xmlhttp.send(param);
}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="code for item category">
var categoryForm = document.getElementById('category-form');
var categoryInput = document.querySelector('.category-item-input');

categoryItemBttn.onclick = function() {
    categoryModal.style.display = 'block';
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = '?command=LIST_CATEGORY'
    xmlhttp.open('GET', url + param, true);
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('result', this.responseText);
            var categories = JSON.parse(this.responseText);
            
            categories.forEach(function(category) {
                var row = document.createElement('tr');
                var cell = document.createElement('td');
                cell.innerHTML = category.name;
                
                row.appendChild(cell);
                categoryTableBody.appendChild(row);
            });
        }
    }
    
    xmlhttp.send();
}

categoryForm.onsubmit = function(event) {
    event.preventDefault();
    console.log('input value', categoryInput.value);
    
    var xmlhttp = new XMLHttpRequest();
    var url = '/InventorySystem/SystemController';
    var param = 'command=ADD_CATEGORY&' + 'param=' + categoryInput.value;
    xmlhttp.open('POST', url, true);
    xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    
    xmlhttp.onreadystatechange = function() {
        if(this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            console.log('result', this.responseText);
            var categoryName = this.responseText;
            
            var row = document.createElement('tr');
            var cell = document.createElement('td');
            cell.innerHTML = categoryName;
            
            row.appendChild(cell);
            categoryTableBody.appendChild(row);
            categoryInput.value = '';
            categoryInput.focus();
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

function createDummyRow(text, colspan) {
    var dummyRow = document.createElement('tr');
    var dummyCell = document.createElement('td');
    dummyCell.setAttribute("colspan", colspan);
    dummyCell.innerHTML = text;
    dummyRow.appendChild(dummyCell);
    
    return dummyRow;
}