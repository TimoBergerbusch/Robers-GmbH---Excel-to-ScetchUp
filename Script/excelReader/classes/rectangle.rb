class Rectangle

  attr_accessor :height, :width, :depth
  attr_reader :id

  def initialize(p_id, p_height, p_width, p_depth)
    @id = p_id
    @height = p_height
    @width = p_width
    @depth = p_depth
  end

  def toString
    return "Rectangle (id: #{@id}): width: #{@width}, height: #{@height}, depth: #{@depth}"
  end

  def createPoints
    points = []
    points << Geom::Point3d.new(0,0,0)
    points << Geom::Point3d.new(@width,0,0)
    points << Geom::Point3d.new(@width,@height,0)
    points << Geom::Point3d.new(@width,0,@depth)
    points << Geom::Point3d.new(0,0,@depth)
    points << Geom::Point3d.new(0,0,0)
    points << Geom::Point3d.new(0,@height,0)
    points << Geom::Point3d.new(@width,@height,0)
    points << Geom::Point3d.new(@width,@height,@depth)
    points << Geom::Point3d.new(0,@height,@depth)
    points << Geom::Point3d.new(0,@height,0)
    return points
  end

  def draw

  end
end