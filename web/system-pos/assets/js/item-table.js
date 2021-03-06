var xmlhttp = new XMLHttpRequest();
var param = '?command=LIST';
var url = '/InventorySystem/SystemController';
xmlhttp.open('GET', url + param, true);

xmlhttp.onreadystatechange = function() {
    if(this.readyState === XMLHttpRequest.DONE && this.status === 200){
        var tableBody = document.querySelector('.pos-table-body');
        var result = JSON.parse(this.responseText);
        console.log('result', result);
        
        var excludedRows = ['id'];
        
        result.forEach(function(item){
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
                    
                    if(key === 'brand' || key === 'category') {
                         tableCell.innerHTML = item[key].name;
                    }
                    
                    tableRow.appendChild(tableCell);
                }
                
                
            }
            
            tableBody.appendChild(tableRow);
        });
    }
};

xmlhttp.send();

var searchBar = document.getElementById('item-search-bar');
searchBar.onkeyup = function() {
    var input, filter, table, tr, td, textValue;
    input = searchBar;
    filter = input.value.toLowerCase();
    table = document.getElementById('pos-table');
    tr = table.getElementsByTagName('tr');
    
    for(var i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName('td')[0]; // refers to the name column of the item table
        
        if (td) {
            textValue = td.textContent || td.innerText;
            
            if (textValue.toLowerCase().indexOf(filter) > -1) {
                tr[i].style.display = '';
            }else {
                tr[i].style.display = 'none';
            }
        }
    }
}
