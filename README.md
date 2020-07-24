Simple configurable mouse automation, useful for simple games that need to mine/craft items

	// 5000 100 backpack.col2 backpack.row1 4 3 backpack.col3 backpack.row3 6000 5 maker.x maker.y
	//  Wait 5000 milliseconds
	//  For 100 times do this:
	//      Goto backpack.col2 backpack.row1
	//      Click once
	//      For 4 times do this:
	//          Click 3 times at backpack.col3 backpack.row3fff
	//          Wait 6000 milliseconds
	//          Click 5 times at maker.x maker.y (to really get any remaining parts if there is lag from the server)

For easier smelting use the inventory slots with:w
    5000 100 inventory.col1 inventory.row1 4 3 inventory.col2 inventory.row1 6000 5 maker.x maker.y
Place the coal in the first slot, place the ore in the second slot.
