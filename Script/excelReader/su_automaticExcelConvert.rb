# Copyright 2013, Trimble Navigation Limited

# This software is provided as an example of using the Ruby interface
# to SketchUp.

# Permission to use, copy, modify, and distribute this software for 
# any purpose and without fee is hereby granted, provided that the above
# copyright notice appear in all copies.

# THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
# IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
# WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
#
# Initializer for Solar North Extension.

require 'sketchup.rb'
require 'extensions.rb'
require 'langhandler.rb'

require_relative 'su_automaticExcelConvert/main.rb'

module AutomaticExcelConvert
  LH = LanguageHandler.new("AutomaticExcelConvert.strings")
  
  extension_name=LH["Robers Excel Convert"]
  
  extension= SketchupExtension.new(extension_name, "su_automaticExcelConvert/main.rb")
  
  extension_description = LH["description"]
  extension.version="0.1"
  extension.creator="Timo Bergerbusch"
  extension.copyright="2018, Timo Bergerbusch"
  
  #Sketchup.register_extension extension, "su_automaticExcelConvert/main.rb"
  Sketchup.register_extension extension, true
end
