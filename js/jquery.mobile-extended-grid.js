/*
* jQuery Mobile Framework : plugin for creating CSS grids
* Copyright (c) jQuery Project
* Dual licensed under the MIT or GPL Version 2 licenses.
* http://jquery.org/license
*/
(function($, undefined ) {
$.fn.grid = function(options){
    return this.each(function(){
        var o = $.extend({
            grid: null
        },options);


        var $kids = $(this).children(),
            gridCols = {solo:1, a:2, b:3, c:4, d:5, e:6, f:7},
            grid = o.grid,
            iterator;

            if( !grid ){
                if( $kids.length <= 7 ){
                    for(var letter in gridCols){
                        if(gridCols[letter] == $kids.length){ grid = letter; }
                    }
                }
                else{
                    grid = 'a';
                }
            }
            iterator = gridCols[grid];

        $(this).addClass('ui-grid-' + grid);

        $kids.filter(':nth-child(' + iterator + 'n+1)').addClass('ui-block-a');
        if(iterator > 1){
            $kids.filter(':nth-child(' + iterator + 'n+2)').addClass('ui-block-b');
        }
        if(iterator > 2){
            $kids.filter(':nth-child(3n+3)').addClass('ui-block-c');
        }
        if(iterator > 3){
            $kids.filter(':nth-child(4n+4)').addClass('ui-block-d');
        }
        if(iterator > 4){
            $kids.filter(':nth-child(5n+5)').addClass('ui-block-e');
        }
            if(iterator > 5){
            $kids.filter(':nth-child(6n+6)').addClass('ui-block-f');
        }
            if(iterator > 6){
            $kids.filter(':nth-child(7n+7)').addClass('ui-block-g');
        }

    });
};
})(jQuery);
