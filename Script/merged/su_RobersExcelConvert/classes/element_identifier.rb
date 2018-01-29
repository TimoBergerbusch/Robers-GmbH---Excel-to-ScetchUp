class ElementIdentifier

  def initialize
    @transformations = Hash.new
    filePath = File.dirname(__FILE__) + "/translations.ini"
    @file = IniFile.load(filePath)

    initializeTransformations
    # change transformationsKey to unique identifier DeSpQ, etc...
    #TODO: read the translations from the iniFile
    #@transformations = {
    #    "DeSpQ" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"},
    #    "DeSpL" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"},
    #    "DE" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"},
    #    "SE" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"},
    #    "KO" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"},
    #    "BOF" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"},
    #    "BOQ" => {"x-achse" => "Breite", "y-achse" => "Länge", "z-achse" => "Höhe"}
    #}
  end

  def identifyElements(elements)
    entities = []
    elements.each {|element|
      entity = identifyElement(element)
      if entity.is_a? nil::NilClass
        puts "ERROR: ElementIdentifier(Code: 0x03): Cannot identify all elements"
        return []
      end

      entities << entity
    }
    return entities
  end

  def identifyElement(element)
    key = getTransformationKey(element["Bezeichnung"], element["Bauteil"])

    if key.is_a?(nil::NilClass)
      puts "ERROR: ElementIdentifier(Code: 0x02): Cannot identify #{element}"
      return NilClass
    end

    x = element[@transformations[key]["x-achse"]].to_i
    y = element[@transformations[key]["y-achse"]].to_i
    z = element[@transformations[key]["z-achse"]].to_i

    #TODO: change to .mm
    return [x, y, z]
  end

  def getTransformationKey(bezeichnung, bauteil)

    #puts "Bezeichnung: #{bezeichnung}, bauteil: #{bauteil}"

    #@file = IniFile.load(@file)

    @file.each_section do |section|
      if @file[section]["kuerzel"] == bezeichnung

        #TODO: erkennt DeSpQ auch als einfachen Deckel
        if @file[section]["bauteil"].is_a?(nil::NilClass) ||
            bauteil.include?(@file[section]["bauteil"].force_encoding(::Encoding::UTF_8))
          #puts "es passt #{@file[section]["key"]}"
          return @file[section]["key"]
          break
        end
      end
    end
  end

  def initializeTransformations
    @file.clone.each_section do |section|
      @transformations[@file[section]["key"].to_s] = {"x-achse" => @file[section]["x-achse"],
                                                      "y-achse" => @file[section]["y-achse"],
                                                      "z-achse" => @file[section]["z-achse"]}
    end
  end
end