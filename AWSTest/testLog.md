module tb_multiplier;

  // Inputs
  reg start;
  reg clk;
  reg reset;
  reg [3:0] a;
  reg [3:0] b;

  // Outputs
  wire [((4*2)/3)+1*4-1:0] bcd;
  wire [4*2-1:0] out;
  wire finish;

  // Instantiate the multiplier module
  multiplier uut (
    .out(out),
    .a(a),
    .b(b),
    .clk(clk),
    .start(start),
    .reset(reset),
    .finish(finish),
    .bcd(bcd)
  );

  // Clock generation
  initial begin
    clk = 0;
    forever #5 clk = ~clk;
  end

  // Test scenario
  initial begin
    $dumpfile("dump.vcd");
    $dumpvars(0, tb_multiplier);

    // Test case 1
    start = 0;
    reset = 1;
    a = 4'b1010;
    b = 4'b0011;
    #10 reset = 0;
    #5 start = 1;
    #100 start = 0;

    // Test case 2
    #10 reset = 1;
    #5 reset = 0;
    a = 4'b1100;
    b = 4'b0101;
    #10 start = 1;
    #100 start = 0;

    #20 $finish;
  end

endmodule# Test Log

This test log documents the response from AWS Rekognition on the test1 to test10 in ```../image```

## Test

| Test | Manual Label | Rekognition's 5 Label |
| -----|---------|------|
|1| Digital Watch, Key | Box, Accessories, Bag, Handbag, First Aid |
|2| Key | Business Card, Paper, Text, Document, Receipt |
|3|Wallet| Accessories, Wallet | 
|4|Laptop| Text, Document, Paper, Bandage, First Aid |
|5|Laptop| Text, Document, Paper |
|6|Mobile Phone| Text, Bandage, First Aid, Tape |
|7|Mobile Phone| Cheese, Food, Document, Receipt, Text |
|8|Electronics| Document, Receipt, Text, Bag |
|9|Credit Card| Text, Accessories, Bag, Handbag, Bandage |
|10|Credit Card| Text, Bandage, First Aid, Bag, Document |
