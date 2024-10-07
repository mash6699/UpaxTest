package com.mash.upax.model.ex

import java.io.IOException

class ApiException (val code: Int, message: String): IOException(message)