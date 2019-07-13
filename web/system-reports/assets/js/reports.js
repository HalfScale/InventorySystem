$(function() {
    
    var reports = [
        {
            module: 'Transaction',
            files: [
                {
                    id: 'transaction-date',
                    label: 'Transaction (Date)',
                    filename: 'TransactionDate',
                    params: []
                },
                {
                    id: 'transaction-month',
                    label: 'Transaction (Month)',
                    filename: 'TransactionMonth',
                    params: []
                },
                {
                    id: 'transaction-year',
                    label: 'Transaction (Year)',
                    filename: 'TransactionYear',
                    params: []
                }
            ]
        },
        {
            module: 'Item',
            files: [
                {
                    id: 'item',
                    label: 'Items',
                    filename: 'Item',
                    params: []
                }
            ]
        }
    ];
    
    buildReportUI(reports);
    
    function buildReportUI(reports) {
        var content = $('.view-content');
        
        reports.forEach(function(report) {
            var container = $('<div>')
                    .addClass('report-container flex-horizontal')
                    .data('module', report.module);
            
            //Report files
            report.files.forEach(function(file) {
                console.log('filename', file.filename);
                var fileDiv = $('<div>')
                        .attr('id', file.id)
                        .data('filename', file.filename)
                        .addClass('report-box flex-vertical clicky');
                
                $('<div>').addClass('report-box-image flex-item-center').appendTo(fileDiv);
                
                var labelContainer = $('<div>').addClass('flex-item-center');
                $('<span>').addClass('report-box-label').text(file.label).appendTo(labelContainer);
                
                labelContainer.appendTo(fileDiv);
                fileDiv.appendTo(container);
            });
            
            container.appendTo(content);
        });
    }
    
    var datepickerDialog = $('#date-picker-dialog');
    var monthpickerDialog = $('#month-picker-dialog');
    var yearpickerDialog = $('#year-picker-dialog');
    
    var pickerDialogs = [datepickerDialog, monthpickerDialog, yearpickerDialog];
    
    var report = {};
    pickerDialogs.forEach(function(picker) {
       
        picker.dialog({
            title: 'Print Transaction',
            modal: true,
            autoOpen: false,
            height: 150,
            widht: 300,
            buttons: [
                {
                    text: 'Print',
                    click: function() {
                        var param = $(this).find('input').val();
                        
                        generateReport(report.module, report.filename, {date: param});
                        console.log('fd result', report);
                    }
                }
            ]
        });
    });
    
    $('#datepicker').datepicker({
        dateFormat: 'yy-mm-dd',
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true
    });
    //<editor-fold defaultstate="collapsed" desc="code for month picker">
        var monthPicker = $('#monthpicker').datepicker({
            changeMonth: true,
            changeYear: true,
            showButtonPanel: true,
            dateFormat: 'yy-mm',
            onClose: function(dateText, inst) {
                var month = $('#ui-datepicker-div .ui-datepicker-month :selected').val();
                var year = $('#ui-datepicker-div .ui-datepicker-year :selected').val();
                $(this).datepicker('setDate', new Date(year, month, 1));
                $('.ui-datepicker-calendar').hide();
            }
        });

        monthPicker.focus(function() {
           $('.ui-datepicker-calendar').hide();
           $('#ui-datepicker-div').position({
              my: 'center top',
              at: 'center bottom',
              of: $(this)
           });
        });
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="code for year picker">
        var yearPicker = $('#yearpicker').datepicker({
            changeYear: true,
            showButtonPanel: true,
            dateFormat: 'yy',
            onClose: function (dateText, inst) {
                var year = $('#ui-datepicker-div .ui-datepicker-year :selected').val();
                $(this).datepicker('setDate', new Date(year, 1));
                $('.ui-datepicker-calendar').hide();
                $('.ui-datepicker-month').hide(); 
            }
        });
        
        yearPicker.focus(function() {
           $('.ui-datepicker-calendar').hide();
           $('.ui-datepicker-month').hide(); 
        });
    //</editor-fold>


    
    $('.report-box').on('click', function() {
        var identifier = $(this).attr('id');
        var reportName = $(this).data('filename');
        var reportModule = $(this).parents('.report-container').data('module');
        
        report['module'] = reportModule;
        report['filename'] = reportName;
        console.log('reportName', reportName);
        console.log('reportModule', reportModule);
        
        var prefix = identifier.split('-')[0];
        
        if (prefix === 'transaction') {
            switch(identifier) {
                case 'transaction-date':
                    console.log('picker start date');
                    datepickerDialog.dialog('open');
                    break;
                case 'transaction-month':
                    console.log('picker start month');
                    monthpickerDialog.dialog('open');
                    break;
                case 'transaction-year':
                    console.log('picker start year');
                    yearpickerDialog.dialog('open');
                    break;
                default: break;
            }
        }else {
            generateReport(report.module, report.filename);
        }
        
    });
    
    
    function generateReport(module, filename, params) {
        var queryString = 'module=' + module + '&filename=' + filename;
        
        if (params != null) {
            queryString += '&params=' + JSON.stringify(modifyDate(params));
        }
        
        console.log('url', queryString);
        
        var xhr = new XMLHttpRequest();
        var url = '/InventorySystem/JasperReportPrint';
        xhr.open('POST', url, true);
        xhr.responseType = 'arraybuffer';
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function () {
            if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
                console.log('success', this.response);
                var file = new Blob([this.response], {type: 'application/pdf'})
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL, '_blank', 'menubar=yes,location=yes,resizable=yes,scrollbars=yes,width=1000,centerscreen,height=800,status=yes');
            }
        };

        xhr.send(queryString);
    }
    
    /**
     * This function returns a modified parameter based on the date value
     * @param {type} params
     * @returns returns a new params, else current params.
     */
    function modifyDate(params) {
        
        if(params.hasOwnProperty('date')) {
            var date = params['date'];
            var split = date.split('-');
            
            switch(split.length) {
                case 1:
                    params['year'] = split[0];
                    delete params['date'];
                    break;
                case 2:
                    params['month'] = split[1];
                    params['year'] = split[0];
                    delete params['date'];
                    break;
                default: break;
            }
        }
        console.log('modified params', params);
        return params;
    }
    
});