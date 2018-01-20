class MaterialHandler

  attr_accessor :materials

  def initialize(materials)
    @material = Hash.new
    if materials.length > 0
      @material["front"] = materials[0]
      @material["back"] = materials[0]
    end

    case materials.length
      when 0
        puts "WARNING: MaterialHandler (Code 0x02): No materials"
      when 1
        @material["left"] = materials[0]
        @material["right"] = materials[0]
        @material["top"] = materials[0]
        @material["bottom"] = materials[0]
      when 2
        @material["left"] = materials[1]
        @material["right"] = materials[1]
        @material["top"] = materials[1]
        @material["bottom"] = materials[1]
      when 3
        @material["top"] = materials[1]
        @material["bottom"] = materials[1]
        @material["left"] = materials[2]
        @material["right"] = materials[2]
      when 6
        @material["back"] = materials[1]
        @material["left"] = materials[2]
        @material["right"] = materials[3]
        @material["top"] = materials[4]
        @material["bottom"] = materials[5]
      else
        puts "ERROR: MaterialHandler(Code 0x01): The number of materials has to be 1,2,3 or 6"
    end
  end

  def get(name)
    return @material[name]
  end

end
