require_relative 'rectangle'

class FaceHandler


  def initialize
    @model = Sketchup.active_model
    @rectangles = []
  end

  def createAndAddRectangle(p_height, p_width, p_depth, p_offset, p_material)
    addRectangle(Rectangle.new(p_height, p_width, p_depth, p_offset, p_material))
  end

  def addRectangle(rectangle)
    if rectangle.is_a? Rectangle
      @rectangles << rectangle
    end
  end

  def drawAll()
    @rectangles.each {|x|
      x.draw(@model)
    }
  end

  def length
    return @rectangles.length
  end
end
