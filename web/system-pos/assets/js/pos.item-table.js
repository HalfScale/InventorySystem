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
                    
                    tableRow.appendChild(tableCell);
                }
                
                
            }
            
            tableBody.appendChild(tableRow);
        });
    }
};

xmlhttp.send();

