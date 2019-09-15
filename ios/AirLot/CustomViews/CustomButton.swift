//
//  CustomButton.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 14/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import UIKit

class CustomButton: UIButton {
    
    @IBInspectable var cornerRadius: Double {
        get {
            return Double(self.layer.cornerRadius)
        }
        set {
            self.layer.cornerRadius = CGFloat(newValue)
        }
    }
}
