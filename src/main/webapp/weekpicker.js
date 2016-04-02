/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    var startDate;
    var endDate;
    var numberOfDays = 6;
    var date;
    var dateFormat;
    var settings;
    var dayOfTheWeek;

    var selectCurrentWeek = function() {
        window.setTimeout(function () {
            $('.week-picker').find('.ui-datepicker-current-day a').addClass('ui-state-active')
        }, 1);
    }

    $('#weekSpinner_input').change(function() {
        numberOfDays = 6 + ((this.value - 1) * 7)
        
        endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - dayOfTheWeek + numberOfDays);
        selectCurrentWeek();
        
        $('.endDate').val($.datepicker.formatDate( dateFormat, endDate, settings));
        $('.week-picker').datepicker("refresh");
    });

    $('.week-picker').datepicker( {
        showOtherMonths: true,
        selectOtherMonths: true,
        minDate: 0,
        firstDay: 1,
        onSelect: function(dateText, inst) { 
            settings = inst.settings;

            date = $(this).datepicker('getDate');
            
            dayOfTheWeek = date.getDay();
            if (dayOfTheWeek == 0) {
                dayOfTheWeek = 6;
            }
            else {
                dayOfTheWeek -= 1;
            }
            
            startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - dayOfTheWeek);
            endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate() - dayOfTheWeek + numberOfDays);
            dateFormat = inst.settings.dateFormat || $.datepicker._defaults.dateFormat;
            
            $('.startDate').val($.datepicker.formatDate( dateFormat, startDate, inst.settings ));    
            $('.endDate').val($.datepicker.formatDate( dateFormat, endDate, inst.settings ));       
            
            selectCurrentWeek();
        },
        beforeShowDay: function(date) {
            var cssClass = '';
            if(date >= startDate && date <= endDate)
                cssClass = 'ui-datepicker-current-day';
            return [true, cssClass];
        },
        onChangeMonthYear: function(year, month, inst) {
            selectCurrentWeek();
        }
    });

    $('.week-picker .ui-datepicker-calendar tr').live('mousemove', function() { $(this).find('td a').addClass('ui-state-hover'); });
    $('.week-picker .ui-datepicker-calendar tr').live('mouseleave', function() { $(this).find('td a').removeClass('ui-state-hover'); });
});


